const fs = require('fs');
const csv = require('csv-parser');
const express = require('express');
const bodyParser = require('body-parser');
const { authorize, addEvent } = require('./googleCalendarService');

const app = express();
app.use(bodyParser.json());

app.post('/add-timetable-events', (req, res) => {
    const csvFilePath = '/output_timetable.csv';
    let events = [];

    fs.createReadStream(csvFilePath)
        .pipe(csv())
        .on('data', (row) => {
            const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
            days.forEach(day => {
                if (row[day]) {
                    const [courseName, slotTime] = row[day].split(" (");

                    // Parse timings from slotTime (e.g., "09:00-10:00")
                    const times = slotTime.split('-');
                    const startTime = times[0];
                    const endTime = times[1].replace(')', '');

                    // Generate start and end times in ISO format
                    const startDateTime = new Date(`2024-10-30T${startTime}:00Z`); // Replace with actual dates
                    const endDateTime = new Date(`2024-10-30T${endTime}:00Z`);

                    // Create an event object
                    const event = {
                        summary: courseName,
                        location: 'University Campus',
                        description: `Class for ${courseName}`,
                        start: {
                            dateTime: startDateTime.toISOString(),
                            timeZone: 'America/Los_Angeles', // Update as per your timezone
                        },
                        end: {
                            dateTime: endDateTime.toISOString(),
                            timeZone: 'America/Los_Angeles', // Update as per your timezone
                        },
                        recurrence: [
                            'RRULE:FREQ=WEEKLY;COUNT=15' // Recurrence example: weekly for a semester
                        ],
                    };
                    events.push(event);
                }
            });
        })
        .on('end', () => {
            // Authorize and add events
            authorize((auth) => {
                events.forEach(event => addEvent(auth, event));
            });
            res.send("Timetable events are being added to your Google Calendar.");
        });
});

// Run the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
