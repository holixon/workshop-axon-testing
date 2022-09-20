#!/bin/bash

#
# Branches
#
declare -a BRANCHES=(
    "initial"
    "class/0-prepare"
    "class/1-command-model"
    "class/2-query-model"
    "class/3-integration"
    "class/4-rest-e2e"
)

echo "--------------------------------------------------"
echo "Githelper script for propagating changes"
echo "across ${#BRANCHES[*]} branches starting with ${BRANCHES[0]}."
echo "--------------------------------------------------"

git fetch --all

COMMAND=""

case "$1" in
  "build")
    echo "Build command detected, will execute build of every branch"
    COMMAND="mvn clean install -T8 -B"
    ;;
  "push")
    echo "Push command detected, will push every branch"
    COMMAND="git push origin"
    ;;
  "pull")
    echo "Pull command detected, will pull every branch"
    COMMAND="git pull origin"
    ;;
  "push-public")
    echo "Push to publi command detected. will push every."
    COMMAND="git push public"
    ;;
  *)
    echo "No command detected, will just merge branches but let them locally. Try $0 build | pull | push | push-public"
    ;;
esac

## now loop through the above array
for (( i = 1; i < ${#BRANCHES[*]}; ++ i ))
do
    PREVIOUS="${BRANCHES[i-1]}"
    CURRENT="${BRANCHES[i]}"

    echo "-----"
    echo "Checking out $CURRENT"
    git checkout $CURRENT
    echo "Merging changes from $PREVIOUS branch to $CURRENT"
    git merge $PREVIOUS --no-edit

    echo "Executing command $COMMAND"
    $($COMMAND)
done

git checkout ${BRANCHES[0]}
