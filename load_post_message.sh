#!/usr/bin/env bash

hey -n 10000 -c 100 -m POST \
  -H "Content-Type: application/json" \
  -d '{ "id": 1, "name": "pedro", "status": true, "tags": [1,2,3] }' \
  http://localhost:8080/message
