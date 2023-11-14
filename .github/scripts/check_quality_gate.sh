#!/bin/bash
# check_quality_gate.sh

SERVICE_NAME=$1
PROJECT_KEY=$2
SONAR_TOKEN=$3

response=$(curl -s -u "${SONAR_TOKEN}:" "https://sonarcloud.io/api/qualitygates/project_status?projectKey=${PROJECT_KEY}")
echo "$response"
qualityGate=$(echo "$response" | jq -r '.projectStatus.status')
echo "Quality Gate status for ${SERVICE_NAME}: $qualityGate"
if [ "$qualityGate" != "OK" ] && [ "$qualityGate" != "NONE" ]; then
  echo "Quality Gate failed for ${SERVICE_NAME}!"
  exit 1
fi
