#!/bin/bash

IS_DEBUG=$(printenv DEBUG)

if [ "${IS_DEBUG}" == "true" ]; then
    export LOG4J_CONFIGURATION_FILE=log4j2_debug.xml
else
    export LOG4J_CONFIGURATION_FILE=log4j2.xml
fi
