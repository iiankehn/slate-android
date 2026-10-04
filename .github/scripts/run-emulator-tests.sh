#!/usr/bin/env bash
set -u

profile_name="$1"
window_size="$2"
window_density="$3"
report_directory="app/build/reports/emulator"

adb shell wm size "$window_size"
adb shell wm density "$window_density"
mkdir -p "$report_directory"

set +e
gradle --no-daemon connectedDebugAndroidTest
test_status=$?
set -e

if [ "$test_status" -ne 0 ]; then
  adb exec-out screencap -p > "$report_directory/${profile_name}-failure.png" || true
  adb logcat -d > "$report_directory/${profile_name}-logcat.txt" || true
fi

exit "$test_status"
