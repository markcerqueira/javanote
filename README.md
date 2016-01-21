# javanote
A Hello World Java project integrated with the Evernote SDK. A place to have a good time and create some notes, notebooks, and stuff. This code was developed and used for an Evernote workshop to introduce non-engineer Evernote employees from around the world to the Evernote API. The [hello-java][23] repository was covered before this repository to gain a basic grasp on Java and APIs. 

For more information on the Evernote SDK, visit the [Evernote Developer Center][20]. Evernote is also hiring so check
out the [Evernote Careers][21] page; let me know if you have any questions about working at Evernote!

## Setting Up

1\. Download this project to your computer by clicking Download ZIP on the main [GitHub page][1] or downloading directly [here][2].

![Download ZIP Image](http://imgur.com/fxven7C.png)

2a\. For users NOT on Yinxiang Biji: create an Evernote account on the [sandbox environment][3]. This user will be SEPARATE from your main Evernote user that runs on the production environment. Sandbox accounts are much easier to integrate with the Evernote API and your data will be safer from your code as well! :) Visit [sandbox.evernote.com/api/DeveloperToken.action][4] to create a Developer Token.

2b\. For Yinxiang Biji (YXBJ) users: create a new YXBJ account [here][7]. YXBJ does not have a sandbox environment. You could use your regular YXBJ log-in information, but it will be safer to have a separate account where you don't care about deleting data. :) If you are logged in already, log out and create a new user so you can play around safely. After creating a new user, visit [app.yinxiang.com/api/DeveloperToken.action][8] to create a Developer Token.

Do not share your Developer token as it gives anyone who knows how to use the Evernote API access to read and modify your account! 

![Developer Token](http://imgur.com/ItF2cAv.png)

3\. Open the project by unzipping it and opening it in IntelliJ IDEA. You can do this from Open in the File menu. 

4\. Copy your Developer Token into the top of Main.java in the String variable DEVELOPER_TOKEN. The line will look something like ``private static final String DEVELOPER_TOKEN = "S=s1:U=91234...";`` Optionally, if you want to run the EDAMDemo you can also paste your Developer Token into the AUTH_TOKEN variable as well.

4b\. For YXBJ only: when creating an EvernoteHelper object you need to use the constructor that points to YXBJ. Your call to construct an EvernoteHelper will look like ``EvernoteHelper evernoteHelper = new EvernoteHelper(DEVELOPER_TOKEN, true /* YXBJ user */)``

5\. Hit run and you should see some output like ``EvernoteHelper - authenticated to username = johnsmith``. 

6\. Add more code to the main() method. Most importantly, have a great time!

## File Walkthrough

* **EDAMDemo.java** - The [demo code][5] available from the Evernote Java SDK. Modified so it can run by calling EDAMDemo.runDemo(). Put your Developer Token in the AUTH_TOKEN variable before running the demo code!
* **EvernoteHelper.java** - A wrapper around the Evernote Java SDK that simplifies calls to the Evernote API.
* **ExchangeRateAPI.java** - A wrapper around the currency exchange APIs provided by [fixer.io][6].
* **LogHelper.java** - A simple helper for writing messages out to the console.
* **Main.java** - The main entry point for code. Only code called from the main() method will get executed!

[1]: https://github.com/markcerqueira/javanote
[2]: https://github.com/markcerqueira/javanote/archive/master.zip
[3]: https://sandbox.evernote.com
[4]: https://sandbox.evernote.com/api/DeveloperToken.action
[5]: https://github.com/evernote/evernote-sdk-java/blob/master/sample/client/EDAMDemo.java
[6]: http://fixer.io/
[7]: https://app.yinxiang.com/api
[8]: https://app.yinxiang.com/api/DeveloperToken.action
[20]: https://dev.evernote.com/
[21]: https://evernote.com/careers/
[23]: https://github.com/markcerqueira/hello-java
