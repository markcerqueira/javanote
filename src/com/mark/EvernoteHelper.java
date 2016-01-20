package com.mark;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.*;

import java.util.List;

/**
 * A nice (hopefully) helper for the Evernote Java API.
 * Adapted (heavily) from EDAMDemo.java: https://github.com/evernote/evernote-sdk-java/blob/master/sample/client/EDAMDemo.java
 */
public class EvernoteHelper {
    private UserStoreClient mUserStoreClient;
    private NoteStoreClient mNoteStoreClient;

    // Private constructor so no one should call this version. Use the constructor below that takes the developer
    // token as a String parameter. DO NOT try calling this method.
    private EvernoteHelper() {

    }

    /**
     *  Constructor for EvernoteHelper. You must call this first before you call anything else as this initializes
     *  the EvernoteHelper object and creates a connection to the user and note store.
     *
     *  EvernoteHelper myFirstEvernoteHelper = new EvernoteHelper("YOUR_DEVELOPER_TOKEN");
     *
     */
    public EvernoteHelper(String developerToken) {
        this(developerToken, false /* by default not Yinxiang Biji */);
    }

    public EvernoteHelper(String developerToken, boolean isYinxiang) {
        // Make sure the caller passed a valid developerToken
        if (developerToken == null || developerToken.length() == 0 || developerToken.equals("PASTE_YOUR_TOKEN_HERE")) {
            throw new RuntimeException("EvernoteHelper - developerToken must be passed!");
        }

        try {
            // Set up the UserStore client and check that we can speak to the server
            EvernoteAuth evernoteAuth;
            if (isYinxiang) {
                evernoteAuth = new EvernoteAuth(EvernoteService.YINXIANG, developerToken);
            } else {
                evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, developerToken);
            }

            ClientFactory factory = new ClientFactory(evernoteAuth);
            mUserStoreClient = factory.createUserStoreClient();

            LogHelper.log("EvernoteHelper - authenticated to username = " + mUserStoreClient.getUser().getUsername());
            LogHelper.newLine();

            // Set up the NoteStore client. We'll need this object to manipulate the notes/notebooks in the user account.
            mNoteStoreClient = factory.createNoteStoreClient();
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }

    /**
     *  Retrieves a list of all notes in the user account and prints them to the console.
     */
    public void listAllNotes() {
        try {
            // Log a message saying we're going to list the notes in the user account
            LogHelper.log("listAllNotes - listing notes in user account: ");

            // First, get a list of all notebooks
            List<Notebook> notebooks = mNoteStoreClient.listNotebooks();

            // Iterate over all notebooks in the user account
            for (Notebook notebook : notebooks) {
                // Log the notebook name first
                LogHelper.log("\tNotebook Name = " + notebook.getName() + "; Notebook GUID = " + notebook.getGuid());

                // Next, search for the first 100 notes in this notebook, ordering by creation date. These next 4 lines
                // create a NoteFilter object that will specify how our search is done.
                NoteFilter filter = new NoteFilter();
                filter.setNotebookGuid(notebook.getGuid());
                filter.setOrder(NoteSortOrder.CREATED.getValue());
                filter.setAscending(true);

                // This call gets the first 100 notes based on the filter parameters defined above.
                NoteList noteList = mNoteStoreClient.findNotes(filter, 0, 100);
                List<Note> notes = noteList.getNotes();

                // Iterate over all notes fetched and print their title to the console
                for (Note note : notes) {
                    LogHelper.log("\t\tTitle = " + note.getTitle() + "; Note GUID: " + note.getGuid());
                }
            }

            // Prints an empty line
            LogHelper.newLine();
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }

    // This is the basic ENML for a simple note. When we create notes we will replace the ${1} with the actual content
    // we want to put in our note. ${1} is on the third line towards the end.
    private static final String CONTENT_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">"
            + "<en-note><span style=\"color:green;\">${1}</span><br/></en-note>";

    /**
     * Create a new note with the specified title and content in the default notebook.
     */
    public void createNote(String title, String content) {
        createNote(title, content, null);
    }

    public void createNote(String title, String content, String notebookGUID) {
        try {
            // To create a new note, simply create a new Note object. This is creating the object LOCALLY. We will
            // fill in all the metadata first and then pass this LOCAL object to the server. The server will do some
            // magic with this note object and hopefully create the note on the server for us.
            Note note = new Note();

            // And fill in whatever you want like the title here...
            note.setTitle(title);

            // If the notebookGUID was specified, put that information in the Note object
            if (notebookGUID != null) {
                note.setNotebookGuid(notebookGUID);
            }

            // And the content here. We use the CONTENT_TEMPLATE above and replace the ${1} with the content we actually
            // want to put in the note
            String noteContent = CONTENT_TEMPLATE.replace("${1}", content);
            note.setContent(noteContent);

            // Finally, send the new note to Evernote using the createNote method. The new Note object that is returned
            // will contain server-generated attributes such as the new note's unique GUID.
            Note createdNote = mNoteStoreClient.createNote(note);

            String createdNoteTitle = createdNote.getTitle();
            String createdNoteGUID = createdNote.getGuid();

            LogHelper.log("createNote - successfully created a new note with title = " + createdNoteTitle + "; note GUID = " + createdNoteGUID);
            LogHelper.newLine();
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }

    /**
     *  Create a new notebook with specified notebook name. Note that if a notebook with the name already exists an
     *  error will be printed as duplicate names for notebooks is NOT allowed by the service.
     */
    public void createNotebook(String notebookName) {
        try {
            // Create a local Notebook object
            Notebook notebook = new Notebook();

            // Set the name
            notebook.setName(notebookName);

            // Tell the server to create our Notebook. The server call returns a notebook object back to us and we
            // store that value in createdNotebook.
            Notebook createdNotebook = mNoteStoreClient.createNotebook(notebook);

            // Log the name of the createdNotebook. Note that the String we get when we call getName() and the notebookName
            // we passed above SHOULD match because we did tell the server to create the notebook with that name.
            LogHelper.log("createNotebook - successfully created a new notebook with name = " + createdNotebook.getName() + "; notebook GUID = " + createdNotebook.getGuid());
            LogHelper.newLine();
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }

    /**
     *  Deletes a note with the specified note GUID. If a note with the GUID does not exist, an error will be printed
     *  to the console.
     */
    public void deleteNote(String noteGUID) {
        try {
            mNoteStoreClient.deleteNote(noteGUID);

            LogHelper.log("deleteNote - deleted note with note guid = " + noteGUID);
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }

    /**
     *  Deletes all notes (up to 1000 notes) in a notebook with the specified notebook GUID.
     */
    public void deleteAllNotesInNotebook(String notebookGUID) {
        try {
            // Get the notebook with the GUID we passed in
            Notebook notebook = mNoteStoreClient.getNotebook(notebookGUID);

            LogHelper.log("deleteAllNotesInNotebook - deleting notes in notebook with name = " + notebook.getName() + "; and notebook GUID = " + notebook.getGuid());

            NoteFilter filter = new NoteFilter();
            filter.setNotebookGuid(notebook.getGuid());

            // This call gets the first 1000 notes in the notebook.
            NoteList noteList = mNoteStoreClient.findNotes(filter, 0, 1000);
            List<Note> notes = noteList.getNotes();

            // Iterate over all the notes in noteList and delete them
            for (Note note : notes) {
                LogHelper.log("\tDeleting note with title = " + note.getTitle() + "; and note GUID = " + note.getGuid());
                mNoteStoreClient.deleteNote(note.getGuid());
            }
        } catch (Exception e) {
            LogHelper.logException(e);
        }
    }
}
