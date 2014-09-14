#The Evil Dead - The Minecraft Mod That Will Eat Your Soul!#

EvilDeadCraft is a Minecraft Mod that brings the Evil Dead from Sam Raimi's classic movies to minecraft.


Clone the project to your computer. 

Open Command Prompt (search cmd in your start menu and open it) and change your directory to the Forge folder that you just created/extracted (your clone).

Perform the following commands:
```
gradlew.bat setupDecompWorkspace
```

Wait for it to say BUILD SUCCESSFUL then preform:
```
gradlew.bat eclipse
```

Replace **eclipse** with **idea** if you are using IntelliJ IDEA.

Now close the Command Prompt. Next you're going to want to make a new folder, name it whatever. This folder will be your workspace that you will actually use within Eclipse. I named mine, "MC Workspace". Leave it empty.

Open up Eclipse and set your workspace to the folder you just created.
Now that you have that done, in your eclipse program, go to File > Import and under General choose Existing Projects Into Workspace



Click 'Next'.

Click "Browse" where it says Select Root Director and choose the main Forge folder you extracted earlier. Then Click Finish.

By now, you should have your workspace setup, along with a nice little example mod, with a couple errors. Just remove the example code in the method and save it.

But wait, I can't run the freaking thing! You're stupid!

I never said we were done. Next, go into the tab "Run" and click "Run Configurations". Once the window pops open, right click Java Application in the sidebar and make a new Run Configuration. Name this, Run Client. For the project, select Forge from the "Browse" button. For the main class, put:
```
net.minecraft.launchwrapper.Launch
```

We're still not done! Go into the arguments tab and add this for program arguments, replacing `forge` with whatever username you use for your forge installation:
```
--version 1.6 --tweakClass cpw.mods.fml.common.launcher.FMLTweaker --userProperties {} --accessToken test
```

That accessToken can be whatever you want; doens't have to be "test". Or you can optionally add `--username YourUsername --password YourPassword` instead of `--accessToken blah` to have forge log you into the Minecraft servers, allowing you to test your mod in a live environment.
If you cannot hear anything when you run your mod, you may need to add `--assetIndex 1.7.10 --assetsDir C:\Users\YourUserName\.gradle\caches\minecraft\assets` to your arguments.

Add in the VM arguments:

```
-Dfml.ignoreInvalidMinecraftCertificates=true
```

Now that we can run the client, we need to add a Run Server option. Go back into the Run Configuration area and make another Java Application, call this, "Run Server".

Select the Forge project for the project area again, and put this for the main class:
```
cpw.mods.fml.relauncher.ServerLaunchWrapper
```

No arguments are needed!

After this, you can optionally go to Help > Eclipse Marketplace and search "Gradle" and install the Gradle IDE, it should be the first one.

You're done!





Directions stolen from sheenrox82's awesome instructions.
```
http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571395-1-7-2-windows-how-to-setup-a-minecraftforge-1-7-2
```

Special shoutout goes to diesieben07 on MinecraftForge.net forums for helping be get my directory structure working set up right! Check out his mod [Cameracraft](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1284425-1-4-7-cameracraft-2-3-take-photos-in-minecraft)!