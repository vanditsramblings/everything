

resource "aws_ami_from_instance" "my_ami" {
  name = "${var.aws_ami_name}"
  source_instance_id = "${var.aws_instance_id}"
}

output "ami_id" {
  value = "${aws_ami_from_instance.my_ami.id}"
}


