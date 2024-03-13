#!/bin/bash

# Get the root directory of the Android project
project_root=$(pwd)

# Navigate to the fonts directory inside the res folder of your Android project
font_directory="$project_root/app/src/main/res/font"
cd "$font_directory" || exit

# Rename each font file to lowercase and replace '-' with '_'
for file in *; do
    if [ -f "$file" ]; then
        newname=$(echo "$file" | tr '[:upper:]' '[:lower:]' | sed 's/-/_/g')
        mv "$file" "$newname"
    fi
done

echo "Font files renamed successfully."
