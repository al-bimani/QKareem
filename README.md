## بسم الله الرحمن الرحيم  

## QKareem  

discord bot that plays Holy Quran chapters in different reciters voice,  
this bot wrriten in java using JDA discord api wrapper and lavaplayer with some utils.  

it's free but not fully completed and i have hope to complete it, as soon as i got free time.  
also i'm planing to write documention and hosting guide.  

### usage

first of all, you should have the following  
- java  
- JDK >= 8  
- gradle  

clone this repository, and add config.txt file in the root of the directory with following content  
```conf
token=
prefix=Q.
lang=en
main_guild_id=
```
`token`: your bot's token  
`preifx`: the prefix to use for commands you can leave it empty for no prefix  
`lang`: use `ar` for arabic use `en` for english.  
`main_guild_id`: this bot is not public and only one guild can use it, include your guild id.  

run `gradle run`  
add the bot to your server  
go ahead and test the bot using `Q.play 1 'yousef noah'`  
where `1` is chapter id and `'yousef noah'` reciter name esacped with `"`  
use `Q.help` for more information.  

use this bot at your own risk i'm not responsiable for any bad use of this bot.
i appreciate your help if you want to contribute in this project, i'm avaliable on discord: `Ahlin Chan#4149`
