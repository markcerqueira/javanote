package com.mark;

public class Main {

    // Get your Developer Token here: https://sandbox.evernote.com/api/DeveloperToken.action
    private static final String DEVELOPER_TOKEN = "PASTE_YOUR_TOKEN_HERE";

    public static void main(String[] args) {
        EvernoteHelper myFirstEvernoteHelper = new EvernoteHelper(DEVELOPER_TOKEN);

        // myFirstEvernoteHelper.listAllNotes();

        // myFirstEvernoteHelper.createNote("New Note Title", "Awesome content");

        // myFirstEvernoteHelper.createNotebook("API Created Notebook");

        // myFirstEvernoteHelper.createNote("Awesome New Note", "Look at me!", "bd840abb-88e4-4adf-9271-d62d802058cb");

        // myFirstEvernoteHelper.deleteAllNotesInNotebook("bf46ba45-7e8f-49e4-b637-691d0b4c8360");

        // ExchangeRateAPI.testExchangeRateAPI();
    }
}
