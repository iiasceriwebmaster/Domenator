import git
import os
from openpyxl import Workbook
from datetime import datetime, timedelta

# Get path to the Git repository where this script is located
repo_path = git.Repo(os.path.dirname(os.path.abspath(__file__))).working_dir

# Open the repository
repo = git.Repo(repo_path)

# Extract commits in reverse order
commits = repo.iter_commits(reverse=True)

# Create Excel workbook
wb = Workbook()
ws = wb.active

# Write headers
ws.append(['№', 'Описание', 'время / минуты', ""])

# Write task data
i = 1
previous_commit_time = None
total_time_spent = timedelta()

for commit in commits:
    if previous_commit_time:
        time_difference = commit.committed_datetime - previous_commit_time
        if time_difference.days >= 1:
            # If the time difference spans across multiple days, subtract the time between 6 PM and 9 AM
            time_difference -= timedelta(hours=15)  # Subtracting 15 hours (9 AM to 6 PM)
    else:
        time_difference = timedelta()  # For the first commit, set the time difference to zero

    total_time_spent += time_difference

    ws.append([i, commit.message, time_difference.total_seconds() // 60, "минут"])
    i += 1

    # Update previous commit time
    previous_commit_time = commit.committed_datetime

# Write total time
ws.append(['','Общее время', total_time_spent.total_seconds() // 60, 'минут'])

# Convert total time to hours
total_hours = total_time_spent.total_seconds() // 3600
ws.append(['', total_hours, 'часов'])

# Write current date/time
ws.append(['', datetime.now(), 'дата/время'])

# Resize columns to fit content
for column_cells in ws.columns:
    length = max(len(str(cell.value)) for cell in column_cells)
    ws.column_dimensions[column_cells[0].column_letter].width = length

# Resize rows to fit content
for row in ws.iter_rows():
    height = max(len(str(cell.value).split('\n')) for cell in row)
    ws.row_dimensions[row[0].row].height = 15 * height

# Save Excel file
wb.save('task_list.xlsx')
