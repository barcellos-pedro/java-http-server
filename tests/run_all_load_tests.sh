#!/usr/bin/env bash

# Run both load test scripts in parallel
./load_post_message.sh &
POST_PID=$!

./load_get_greeting.sh &
GET_PID=$!

# Wait for both to finish
wait $POST_PID
wait $GET_PID

echo "Both load tests finished."
