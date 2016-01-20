# javanote
A Hello World Java project integrated with the Evernote SDK

## Setting Up

1\. Download this project to your computer by clicking Download ZIP on the main [GitHub page][1] or downloading directly [here][2].

![Download ZIP Image](http://imgur.com/fxven7C.png)

2\. Create an Evernote account on the [sandbox environment][3]. This user will be SEPARATE from your main Evernote user that runs on the production environment. Sandbox accounts are much easier to integrate with the Evernote API and your data will be safer from your code as well! :) 

3\. Visit [https://sandbox.evernote.com/api/DeveloperToken.action][4] to create a Developer Token. Do not share this as it gives anyone who knows how to use the Evernote API access to read and modify your account! 

![Developer Token](http://imgur.com/ItF2cAv.png)

4\. Copy your Developer Token into the top of Main.java in the String variable DEVELOPER_TOKEN. If you want to run the EDAMDemo you can also paste your Developer Token into the AUTH_TOKEN variable.

5\. Add some code to the main() method in Main.java and hit run!

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
