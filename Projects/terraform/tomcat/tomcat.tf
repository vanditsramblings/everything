
resource "aws_instance" "tomcat" {
  ami           = "${var.aws_base_ami_id}"
  instance_type = "${var.aws_base_instance_type}"
  key_name 		= "${var.aws_key_name}"
  
  provisioner "remote-exec" {
      inline = [
	  "sudo apt-get update",
	  "sudo apt-get install -y --no-install-recommends unzip",
	  "sudo apt-get clean",
	  "sudo mkdir -p /home/tomcat ",
	  "sudo chown -R ubuntu: /home/tomcat"
	  ]
	  connection {
	        type= "ssh"
            user = "ubuntu"
			private_key = "${file(var.aws_pem_file)}"
        }
    }
	
  provisioner "file" {
        source = "./"
        destination = "/home/tomcat"
		 connection {
		    type= "ssh"
            user = "ubuntu"
            private_key = "${file(var.aws_pem_file)}"
        }
    }
	
	provisioner "remote-exec" {
      inline = [
	  "cd /home/tomcat/tomcat/bin",
      "sudo chmod +x *",
	  "nohup startup.sh"
	  ]
	  connection {
	        type= "ssh"
            user = "ubuntu"
			private_key = "${file(var.aws_pem_file)}"
        }
    }
}

resource "aws_security_group" "tomcat_sg" {
  name = "tomcat_sg"
  
   ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
	self="true"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
   lifecycle {
    create_before_destroy = true
  }
}


output "tomcat_public_ip{
  value = "${aws_instance.tomcat.public_ip}
}


output "tomcat_instance_id{
  value = "${aws_instance.tomcat.id}"
}

	
	

