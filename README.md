# Space-Invaders

main class SpaceInvaders/main.java

This project was an open ended project i started with no real plan of what it was going to be, i started it as the classic space invaders arcade game,
but as i had made one of these in the past i didnt want to just rehash it, at first i did the spaceinvaders generic form but i soon just wanted to do as 
many new things as possible in regards to coding in java.

Because there was no plan per say and didnt know how far i would be taking it i made a few errors in regards to the project format, first i never had a sprite class
and made a couple of levels without it, as i got further into the project and a theme was emerging i went back a created a sprite class to which the enemies sprites
extended, i did this after the first couple of enemies as i had intertwined some with the level so much it was more hassle to extend the sprite class as the whole
level would need doing, i did this for one enemy because of an item drop it was going to do and it was worth it.
though a solid lesson was learned in extending classes that couldnt be taught 2nd hand, from there i began to do the same for the player ship itself as it gets 
upgraded in the game, then i did it for the item drops, it made the whole code so much easier and versatile, the rest of the game went alot more quicker and smoothly.
this also spurred me to properly package all the classes in the project it was something again i should have done from the start, but it was a hard learned lesson,
the result looks so much better and is very readable. one thing i didnt do but thought of later was thagt i should have done the levels the same way, creating a level
sprite that all the levels inherited from, as well as stored them in an array and just increased a variable to read the nextr level in the array, this would have made
the board class paintComponent method much more cleaner.
i tried to make it that every enemie and level had a different feature i had to code to extend my learning, i think i made to many threads running on some of them so i did the next one with one thread using do while loops using instant to basically hold the thread for a given ammount of time, i used alot of paint.net to customise images i got fromm online, one of the hardest things i had done was the lazer pincer movement of the cloner enemy spaceship, i used the line2D class to draw the lines and get there bounds and using the affintransform graphics method to move them in pincer movement, this took a while to master and get just right, but i am very happy with it, also many of the homing missiles were a bit tricky using the Math sine and cosine.


THE GAME


player Movement:

use the arrow keys,   spacebar to fire missiles

Start:

running the game move the cursor to the insert coin image.. and insert a coin (left click)
you have 3 lives

level 1:

you are flying through an asteroid field, destroy all the asteroids to move to the next level,
the bigger the asteroid the more hits it takes to destroy it, being hit by asteroid destroys the asteroid and player takes damage.

level 2:

you meet the abandoned ship, two items given, cannot be dodged
shield - will activate itself
nuke - will load instantly,  fire by pressing the 'N' button  30 second cool down

aliens attack old school space invaders style, destroy them all to move to next level

level 3:

you meet the mystery craft that challenges you to battle
on defeating the craft you are given a message to give a call if you need help,
on level 5 after the first cloner is destroyed a blue 'M' will appear press the 'M' key to call ship for help.

level 4:

you approuch a planet and an alarm sounds time to fight the planets ship.
ship moves like a pong ball, has missiles aimed for you, dont stay still!
on defeat you will go to the planet and be given a new ship. controls shown.

level 5:

An enemy cloner ship enters via portal to fight,  it can teleport, it has homing missiles that will follow you, you can destroy them!, beware the lazer pincer movment.
you need to destroy it 3 times to complete the level
watch for the 'M' for help from the mystery craft!

Level 6:

classic space invader enemy figure made of individual pixels, destroy the middle pixel to destroy the ship and drop its shield, careful it bouces around do not let it touch you!  even when its destroyed and the shield is down the pixels will give you a shock dont get too close!
3 to kill!


GAME OVER
