	# Summer Independent Study Journal

## June 17th, 2017

### Work Times
In: 1:05 PM  
Out: 3:05 PM  

In: 5:10 PM  
Out: 6:24 PM  

### Total Time Worked
3:14

### Work Done
Cleaned out and reorganized home PC files to make future work easier  
Created Github repository  
Linked local Git repository to GitHub repository  
Created IntelliJ Project  
Setup .gitignore file  
Choose and added a license to the project  
Added brief description in README.md file
Created journal.md file and decided on a format  
Started following cmacfadyen on GitHub  
Informed Charlie of progress  

### Learning Done
Refamiliarized self with Git commands    
Learned how the .gitignore file works  
Learned how to connect local repository to GitHub  
Learned how to do as few basic things in Markdown  

## June 19th, 2017

### Work Times
In: 1:20 PM  
Out: 7:41 PM

### Total Time Worked
6:21

### Work Done  
Before the independent study began, I had started preliminary work on a visualization of the Taylor series. Today, I incoporated the code I wrote during that work into the git repository for the main project. This included code to represent and evaluate functions and code to create a a very basic GUI and render a graph of a function.  

The code to represent and evaluate functions was solid, however the rendering mechanism was not. The graphs it created were hoplessly jagged and of inconsistent thickness. Therefore, I rewrote the render method, making use of JavaFX's prexisting methods for drawing curves. I choose to do this, rather than create the graph entirely from scratch as I originally planned, because learning how to use antialiasing and other neccesary techniques would have been a very distraction from the main focus of the independent study which is calculus.  

Additionally, I wrote code to allow the graph to be panned and zoomed using the mouse and mouse wheel. This took by far the most time (>90%) of the work session. It took so long mostly because I kept running into subtle errors that invalidated what I thought was a workable method.

Finally, I updated the journal to reflect the days progress.

### Learning Done
Today, I read about JavaFX's event system and the documentation for numerous methods and fields contained within the MouseEvent and ScrollEvent classes. I also read about the methods of the GraphicsContext class and many Stack Overflow posts about the MOUSE_DRAGGED event although none were helpful.

## June 21th, 2017

### Work Times
In: 11:20 PM  
Out: 1:27 AM (Next Day)

### Total Time Worked

2:07

### Work Done  
Began serious work on the calculus side of things:  
Started work on the render taylor series function, works except for serious bug in the way functions are evaluated which prevents it from working for polynomials of more than two terms  
Wrote recursive method to evaluate nth derivative of a function  
Wrote recursive method to evaluate factorials
Changed the render function method to render at a higher level of detail when zoomed in. Prevents functions looking blocky at high zooms.
Added support for exponentiation to function evaluation system

### Learning Done
Today I mostly worked in code, however I researched the precedence and associativity of the exponentiation observation and referenced the formula for the Taylor series

## June 22th, 2017

### Work Times
In: 12:31 PM  
Out: 3:37 PM
In: 10:10 PM    
Out: 12:08 AM (Next Day)  

### Total Time Worked
5:04

### Work Done  
Made the render Taylor Series Button actually work*  
I fixed multiple issues with the implementation of the shunting yard algorithm which were previously causing the miscalculation of certain expressions
Worked to get the GitHub repository in better working order:  
I added issues, tasks to the wiki, and a subproject for the component of the independent study I am working on right now.  
I linked my home Git commits to my GitHub username.       
I added a timer to the render method so I can easily see how changes affect its speed.  
I changed the derivative method to use symetric differences.
And, although implementing fully javadoc comments is undoubtedly a work in progress, I generated a set of them as a test. 


### Learning Done
Today, I researched the exact implementation of the Shunting Yard algorithm especially with regards to operator precedence and associativity. I also watched several videos on Taylor's remainder theorem and researched optimum methods for machine differentiation. On the latter point, I found no satisfactory solution to the problem I face. 

## June 24th, 2017

### Work Times
In: 9:35 PM
Out: 12:12 AM (Next Day) 

### Total Time Worked
2:37

### Work Done
Wrote experimental code for duel number computation.

### Learning Done
Today, I spent time on the difficult issue of the accurate compuatation of higher order derivatives in code. I read and watched videos about automatic differentiation and duel numbers and especially their extension to higher dimensions.

## June 27th, 2017

### Work Times
In: 4:30  PM
Out: 5:20 PM

### Total Time Worked
0:50

### Work Done
Yesterday I arrived in California, where I will be staying with my Grandparents fo several weeks. So today I setup Git, IntelliJ and the Repository on their computer.

### Work Done
Yesterday I arrived in California, where I will be staying with my Grandparents fo several weeks. So today I setup Git, IntelliJ and the Repository on their computer.

## June 28th, 2017

### Work Times
My work today was divided into 3 sessions of roughly equal length, unfortunately I lost t
Out: 12:41 AM(Next day)
### Total Time Worked
5:00 (estimated, likely understatement) 
### Work Done
Today, I worked on creating the UI that would combine all the tools I have built and will build into one smooth and visually appealing application.   
I created a method to do all rendering updates without having to write out 6 lines of code every time  
I wrote several other methods like this to clean up the code base  
I created a Point class and a method for graphing those points
I added the ability to graph derivatives.

### Learning Done
I spent large ammounts of time reading through the JavaFX documentation on various nodes as I was creating the UI


## June 29th, 2017

### Work Times
In: 5:39 PM  
Out: 6:25  
In 7:02  
Out 9:17  
### Total Time Worked
3:29
### Work Done
Today, I created the first iteration of the derivative visualization. I wrote code to allow secant lines to be drawn with varying distances between the points. 
I added support for negative numbers and subtraction in the Function class

### Learning Done
I learned how to use observable properties in javaFX, that is write code that acts when one of an objects field changes. I also learned about the Slider class.

## July 4th, 2017

### Work Times
In: 11:44 AM (West Coast Time)  
Out: 4:02 PM (Mountain Time) 
In: 10:10 PM  
Out: 12:28 PM (next day)  

### Total Time Worked
   5:36

### Work Done
Separated code for secant lines, derivative graphs and the Taylor Series into classes  
Added UI for Taylor Series, included options to choose order, the point to approximate and whether it is a maclaurin series  
Attempted without success to fix sudden loss of control and pin wheel of death

### Learning Done

## July 5th, 2017

### Work Times
In: 11:51 AM
Out: 2:16 PM
In: 10:04 PM
Out 10:34 PM

~1:00 In small chunks while driving through Bryce Canyon National Park

### Total Time Worked

### Work Done
Worked on the Riemann sum visualization

### Learning Done

## July 6th, 2017

### Work Times
In: 10:10 AM
Out: 10:40 AM
In: 11:15 AM
Out: 12:30 PM (West Coast Time)

### Total Time Worked

### Work Done
Added purple bars to demarcate the zone of Riemann summation  
Added feature to the Taylor Series visualization that allows the user to see the error of the polynomial approximation.  

### Learning Done

## July 7th, 2017

### Work Times
In: 11:45 AM
Out: 12:34 PM
In: 1:43 PM

### Total Time Worked

### Work Done
Fixed bug causing occasional catastrophic errors, preventing further execution of the program, when using the secant line visualizer.  
Created method to round output values before displaying them to the user.
Switched the render axis method over to the new method of drawing lines.  
Improved commenting.  
 
### Learning Done

## August 3rd, 2017

### Work Times

In: 11:10  
Out: 12:05
In: 1:40
Out: 3:55
In: 5:24
Out: 6:30

### Total Time Worked
4:16

### Work Done
I wrote html and css code to create the website http://bayfoleycox.com/. The purpose of the site is to make the calculus visualizer 
more accesible. Unfortunately, due to internet browsers prioritizing security over Oracle's well being, it only runs on internet exploer
and maybe Safari. Even in these browsers, you have to make an exemption to security settings for http://bayfoleycox.com/ in order for the code to run.

### Learning Done
I did intense research on a wide variety of webhosting services. I eventually found that Github pages was the best  webhosting
service available in my (very low) rice range. I learned how to configure DNS records to connect my domain, purchased through Hover to 
my Github pages account. I also refamiliarized myself with basic html, including going beyond what we learned in class, incoporating 
css and multiple pages. 

## August 7th, 2017

### Work Times

In: 1:40PM  
Out: 5:12 PM  

### Total Time Worked
3:32  
### Work Done
Fixed the (formerly) flawed system of expression input:
Added support for mathamtical constants pi & e  
Added support for the cos, tan, sqrt, ln, log, abs(including support for the use of |  |), floor, and ceiling functions
Fixed issue where unparsable expresssions caused an infinite loop stopping execution  
Fixed issue where parseable but invalid (e.g "sin(x^/)") would run. Now, when these expressions are entered, an error message is shown and the mainfunction reset.  
Fixed (for now) issue where functions with limited domains rendered with solid blocks of color. 

### Learning Done

## August 8th, 2017

### Work Times
In: 3:25PM  
Out: 7:52PM  
In: 8:44PM  
Out: 8:58PM      
In: 10:16PM  
Out: 12:18AM (next day)  
### Total Time Worked
6:43

### Work Done
Switched console to use a text flow pane enabling greater control of style. This allowed me to implement red colored error messages.  
Implemented responsive design to allow all UI elements to scale correctly. 
### Learning Done

## August 10th, 2017

### Work Times
In: 10:30PM
Out: 11:50PM
### Total Time Worked
1:20
### Work Done
Made UI elements dynamically change color to match tab background.  
Created a settings menu.  

## August 11th, 2017

### Work Times
In: 12:20PM
Out: 2:49PM
In: 4:10PM 
Out: 5:26
### Total Time Worked

### Work Done
Added ability for user to customize UI colors  
Added ability for * to be inferred from token list 
Made use of safeRender function dependent to improve performance 
Fixed vertical resizing changing aspect ratio
Fixed issue with ugly text reflow during horizontal scaling.

## August 12th, 2017

### Work Times
In: 1:40PM
Out: 4:26PM
### Total Time Worked

### Work Done
Fixed text lengths to reduce size of the tool TabPane
Fixed issue where bottom and right edges did not appear on start up
Added buttons to reset window and reset origin.  
Fixed isMaclaurin checkbox so that when activated it resets the xPos of the taylor approximation to zero.
Fixed issue where slider values were clipped to keep them visible resulting in confusing visuals.
Fixed issue where integral lower bound marking line protruded above point in an ugly manner and rewrote system using the drawLineSegment method.  

