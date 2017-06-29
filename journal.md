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

### Total Time Worked
0:50

### Work Done
Yesterday I arrived in California, where I will be staying with my Grandparents fo several weeks. So today I setup Git, IntelliJ and the Repository on their computer.

### Work Times
In: 4:30  PM
Out: 5:20 PM

### Total Time Worked
0:50

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

