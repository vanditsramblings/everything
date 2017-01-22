#/bin/bash

function usage() {
  echo "Usage: generate_ami.sh"
  echo "[-n|--name]     :       AMI Name [Mandatory]"
  echo "[-v|--var]      :       Pass terraform vars using this option .(Ex : -var \"key=value\")[Pass secret key, access key ,region , etc through this option]"
}


AMI_NAME="na"
VARIABLES_ARR_COUNT=0

while [[ $# -gt 0 ]]; do
    key="$1"
    case "$key" in
		-n|--name)
        shift
        AMI_NAME="$1"
        ;;
        -n=*|--name=*)
        AMI_NAME="${key#*=}"
        ;;
		--var)
		shift
		VARIABLES_ARR[VARIABLES_ARR_COUNT]=$1
		VARIABLES_ARR_COUNT=$VARIABLES_ARR_COUNT+1
        ;;
		--var=*)
		VARIABLES_ARR[VARIABLES_ARR_COUNT]=${key#*=}
		VARIABLES_ARR_COUNT=$VARIABLES_ARR_COUNT+1
        ;;
        *)
        echo "Invalid Option '$key'"
        ;;
    esac
    shift
done

MISSING_ARGS="-"
FIRST=1

if [ "$AMI_NAME" = "na" ]
then
  echo "Please enter the AMI NAME"
  usage
  exit 1
fi


echo "AMI NAME : $AMI_NAME"
echo 

echo "Generating AMI..."

VAR_STR=""
for i in ${VARIABLES_ARR[@]}
do
  VAR_STR+="-var \"$i\" "
done

#Initializing modules
terraform get

echo "Starting Instance ..."
TF_CREATE_INSTANCE_CMD="terraform apply $VAR_STR -target=\"module.instance_module.aws_instance.my_instance\" -state=./instance_state.tfstate "

eval $TF_CREATE_INSTANCE_CMD

echo "Generating AMI..."

INSTANCE_ID=$(terraform output -state=./instance_state.tfstate instance_id)

TF_CREATE_AMI_CMD="terraform apply -var=\"aws_instance_id=$INSTANCE_ID\" -var \"aws_ami_name=$AMI_NAME\" $VAR_STR -target=\"module.ami_module.aws_ami_from_instance.my_ami\" -state=./ami_state.tfstate"

eval $TF_CREATE_AMI_CMD

AMI_ID=$(terraform output -state=./ami_state.tfstate ami_id )

echo "Destroying the intermediatry Instance. ID: $INSTANCE_ID ..."

TF_DELETE_INSTANCE_CMD="terraform destroy -force $VAR_STR -target=\"module.instance_module.aws_instance.my_instance\" -state=./instance_state.tfstate"

eval $TF_DELETE_INSTANCE_CMD

echo "Intermediatery Instance. ID: $INSTANCE_ID destroyed ..."

echo "Processing complete.AMI created : $AMI_ID"
