const fs = require('fs');
const { google } = require('googleapis');

const SCOPES = ['https://www.googleapis.com/auth/calendar'];
const TOKEN_PATH = 'token.json';

// Load client secrets from a local file.
function authorize(callback) {
    fs.readFile('credentials.json', (err, content) => {
        if (err) return console.log('Error loading client secret file:', err);
        const credentials = JSON.parse(content);
        const { client_secret, client_id, redirect_uris } = credentials.installed;
        const oAuth2Client = new google.auth.OAuth2(client_id, client_secret, redirect_uris[0]);

        // Check if we have previously stored a token.
        fs.readFile(TOKEN_PATH, (err, token) => {
            if (err) return getAccessToken(oAuth2Client, callback);
            oAuth2Client.setCredentials(JSON.parse(token));
            callback(oAuth2Client);
        });
    });
}

// Get and store new token if needed.
function getAccessToken(oAuth2Client, callback) {
    const authUrl = oAuth2Client.generateAuthUrl({
        access_type: 'offline',
        scope: SCOPES,
    });
    console.log('Authorize this app by visiting this URL:', authUrl);
    const readline = require('readline').createInterface({
        input: process.stdin,
        output: process.stdout,
    });
    readline.question('Enter the code from that page here: ', (code) => {
        readline.close();
        oAuth2Client.getToken(code, (err, token) => {
            if (err) return console.error('Error retrieving access token', err);
            oAuth2Client.setCredentials(token);
            // Store the token to disk for later program executions
            fs.writeFile(TOKEN_PATH, JSON.stringify(token), (err) => {
                if (err) console.error(err);
                console.log('Token stored to', TOKEN_PATH);
            });
            callback(oAuth2Client);
        });
    });
}

function addEvent(auth, event) {
    const calendar = google.calendar({ version: 'v3', auth });
    calendar.events.insert({
        auth: auth,
        calendarId: 'primary',
        resource: event,
    }, (err, res) => {
        if (err) return console.log('Error adding event:', err);
        console.log('Event created: %s', res.data.htmlLink);
    });
}

module.exports = {
    authorize,
    addEvent,
};
