import asyncio
import aiofiles
import re
import json
import csv
import io

def fix_message_in_slice(slice_str: str) -> str:
    marker = '"message":"'
    idx = slice_str.find(marker)
    if idx == -1:
        return slice_str

    start = idx + len(marker)
    end_quote_index = slice_str.find('"', start)

    if end_quote_index == -1:

        if slice_str.rstrip()[-1] == '}':
            message_value = slice_str[start:-1]
            fixed_message = message_value.replace("{", "<").replace("}", ">").replace('"', "'")
            return slice_str[:start] + fixed_message + '"' + "}"
        else:
            message_value = slice_str[start:]
            fixed_message = message_value.replace("{", "<").replace("}", ">").replace('"', "'")
            return slice_str[:start] + fixed_message + '"'

    else:
        message_value = slice_str[start:end_quote_index]
        fixed_message = message_value.replace("{", "<").replace("}", ">").replace('"', "'")
        return slice_str[:start] + fixed_message + slice_str[end_quote_index:]

async def read_from_file(path: str) -> str:
    async with aiofiles.open(path, mode='r', encoding='utf-8') as f:
        content = await f.read()
    return content

async def extract_logs(text: str) -> list:
    pattern = r'\{.*?\}'
    slices = re.findall(pattern, text)
    return slices

async def convert_logs(slices: list) -> list:
    logs_json = []

    for slice_str in slices:

        try:
            log_obj = json.loads(slice_str)
            logs_json.append(log_obj)
        except json.JSONDecodeError:
            if '"message":"' in slice_str:
                fixed_slice = fix_message_in_slice(slice_str)
                try:
                    log_obj = json.loads(fixed_slice)
                    logs_json.append(log_obj)
                    continue
                except json.JSONDecodeError:
                    pass
            print(f"Unable to convert: {slice_str}")
    return logs_json

async def write_to_csv(logs: list, out: str):
    keys_set = set()

    for log in logs:
        keys_set.update(log.keys())

    keys_set = list(keys_set)

    spreadsheet = io.StringIO()
    writer = csv.DictWriter(spreadsheet, fieldnames=keys_set)
    writer.writeheader()

    for log in logs:
        writer.writerow(log)

    async with aiofiles.open(out, mode='w', encoding='utf-8') as csv_file:
        await csv_file.write(spreadsheet.getvalue())

async def main():
    content = await read_from_file("app_log.log")
    slices = await extract_logs(content)
    logs = await convert_logs(slices)
    await write_to_csv(logs, "logs.csv")

    print("OK")

if __name__ == "__main__":
    asyncio.run(main())
