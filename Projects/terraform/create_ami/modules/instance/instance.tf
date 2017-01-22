
resource "aws_instance" "my_instance" {
  ami           = "${var.aws_base_ami_id}"
  instance_type = "${var.aws_base_instance_type}"
  key_name      = "${var.aws_key_name}"
  }

output "instance_id" {
  value = "${aws_instance.my_instance.id}"
}

	
	

