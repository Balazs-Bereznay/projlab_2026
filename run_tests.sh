#!/bin/bash
# Test runner script
PASS=0
FAIL=0
ERRORS=()
DIFF_ARGS=(-u)

# Enable colorized diff when supported and output is a TTY.
if [ -t 1 ] && diff --help 2>&1 | grep -q -- '--color'; then
  DIFF_ARGS+=(--color=always)
fi

normalize() {
    # Strip CR characters, remove trailing empty lines
    tr -d '\r' < "$1" | awk 'NF{found=NR} {lines[NR]=$0} END{for(i=1;i<=found;i++) print lines[i]}'
}

for input_file in test/input/*.txt; do
  base=$(basename "$input_file" .txt)
  expected_file="test/expected/${base}_elvart.txt"

  if [ ! -f "$expected_file" ]; then
    continue
  fi

  # Clear temp.txt
  rm -f temp.txt

  # Run the program with this input
  echo "reset
load ${base}.txt" | java -Dfile.encoding=UTF-8 -cp target/classes model.Prototipus > /dev/null 2>&1

  # Check if temp.txt was created
  if [ ! -f temp.txt ]; then
    FAIL=$((FAIL+1))
    ERRORS+=("FAIL [no output]: $base")
    continue
  fi

  # Normalize both files and compare
  if diff "${DIFF_ARGS[@]}" <(normalize temp.txt) <(normalize "$expected_file") > /dev/null 2>&1; then
    PASS=$((PASS+1))
  else
    FAIL=$((FAIL+1))
    ERRORS+=("FAIL: $base")
    echo "=== DIFF for $base ==="
    diff "${DIFF_ARGS[@]}" --label "actual: temp.txt" --label "expected: $expected_file" \
      <(normalize temp.txt) <(normalize "$expected_file")
    echo ""
  fi
done

echo ""
echo "Results: $PASS passed, $FAIL failed"
for err in "${ERRORS[@]}"; do
  echo "  $err"
done
