# pip install pyperclip
import re
import pyperclip
import datetime
import os
import sys

credentials_path = os.path.expanduser("~/.aws/credentials")
print(f"Updating at {datetime.datetime.now()}, \n\nScript will read form your clipboard and replace variables in {credentials_path}\nPress Ctrl-C to stop or enter to continue\n")
try:
    input()
except KeyboardInterrupt:
    print("\nOperation cancelled by user.")
    sys.exit()

def update_or_add_variable(content, variable, new_value):
    pattern = re.compile(rf'^{variable}=(.+)$', re.MULTILINE)
    replacement = f'{variable}={new_value}'
    if pattern.search(content):
        return pattern.sub(replacement, content)
    else:
        return content + '\n' + replacement if content else replacement

clipboard_content = pyperclip.paste()

required_variables = {
    'aws_access_key_id': r'aws_access_key_id=(\S+)',
    'aws_secret_access_key': r'aws_secret_access_key=(\S+)',
    'aws_session_token': r'aws_session_token=(\S+)'
}

values = {}
for var, pattern in required_variables.items():
    match = re.search(pattern, clipboard_content)
    if match:
        values[var] = match.group(1)

if len(values) != len(required_variables):
    missing = set(required_variables.keys()) - set(values.keys())
    print(f"Missing variables: {', '.join(missing)}")
    exit()


with open(credentials_path, 'r') as file:
    content = file.read()

for var, value in values.items():
    content = update_or_add_variable(content, var, value)

with open(credentials_path, 'w') as file:
    file.write(content)


for var, value in values.items():
    print(f"{var}={value}")

print(f"\nCredentials updated successfully. At {datetime.datetime.now()}\n")