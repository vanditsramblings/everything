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

variable "aws_key_name" {
  type = "string"
  default = ""
  description = "AWS Instance Key name"
}

variable "aws_base_ami_id" {
  type="string"
  default="ami-f9fb8c96"
  description="Base ami ID.Defaults to Ubuntu."
}

variable "aws_base_instance_type" {
  type="string"
  default="t2.micro"
  description="Base ami Type.Defaults to t2.micro."
}

variable "aws_pem_file" {
  type="string"
  default=""
  description="Private key file(fully qualified path)"
}




