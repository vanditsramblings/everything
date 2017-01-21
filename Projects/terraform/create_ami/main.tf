
provider "aws" {
  access_key = "${var.aws_access_key}"
  secret_key = "${var.aws_secret_key}"
  region     = "${var.aws_region}"
}

module "instance_module" {
  source = "./modules/instance"
  aws_pem_file="${var.aws_pem_file}"
}


module "ami_module" {
  source = "./modules/ami"
  ami_name = "${var.ami_name}"
  aws_access_key = "${var.aws_access_key}"
  aws_secret_key = "${var.aws_secret_key}"
  aws_region     = "${var.aws_region}"
  aws_instance_id= "${var.aws_instance_id}"
}

output "instance_id" {
  value = "${module.instance_module.instance_id}"
}

output "ami_id" {
  value = "${module.ami_module.ami_id}"
}
