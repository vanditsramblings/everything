variable "aws_ami_name" {
   type = "string"
   default = "my_ami"
   description = "AMI Name"
}

variable "aws_region" {
  type = "string"
  default = "us-west-1"
  description = "AWS Region"
}

variable "aws_access_key" {
  type = "string"
  default = ""
  description = "AWS Access Key"
}

variable "aws_secret_key" {
  type = "string"
  default = ""
  description = "AWS Secret key"
}

variable "aws_instance_id" {
  type = "string"
  default = ""
  description = "Started instance ID"
}
