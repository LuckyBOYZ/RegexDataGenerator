#!/bin/bash

export JAVA_ARGS="iterationFieldName=${ITERATION_FIELD_NAME:-iteration} \
iterationNumber=${ITERATION_NUMBER:-10} \
jsonFileName=${JSON_FILE_NAME:-/app/file/file.json} \
prefixAndSuffix=${PREFIX_AND_SUFFIX:-$} \
specialInputDataSeparator=${SPECIAL_INPUT_DATA_SEPARATOR:-|} \
isFormattedResult=${IS_FORMATTED_RESULT:-false}"
