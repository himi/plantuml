FILE=$1
USERCRED=$2
BRANCH=$3
URI=$4

#RES=$(git hash-object --no-filters $FILE)
#SHA=${RES%% *}
SHA=$(curl -X GET $URI | jq .sha)

echo "FILE: $FILE, u: $USERCRED, BRANCH: $BRANCH ==> $URI"

base64 $FILE | jq -R --slurp "{\"message\": \"update\", \"sha\": ${SHA}, \"branch\": \"${BRANCH}\", \"content\": . }" | curl -XPUT -u $USERCRED -d@- $URI
