

resource "aws_ami_from_instance" "be_ami" {
  name = "${var.be_ami_name}"
  source_instance_id = "${var.aws_instance_be_id}"
}

output "be_ami_id" {
  value = "${aws_ami_from_instance.be_ami.id}"
}
