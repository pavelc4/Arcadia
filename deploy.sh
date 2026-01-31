#!/bin/bash
set -e

echo "🔨 Building Arcadia..."
# Check if gradlew exists, otherwise try gradle
if [ -f "./gradlew" ]; then
    ./gradlew clean installDebug
else
    gradle clean installDebug
fi

echo "🚀 Launching DevActivity..."
adb shell am start -n com.android.arcadia/.DevActivity
