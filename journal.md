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

## June 22th, 2017

### Work Times
In: 12:31 PM  
Out: 3:37 PM

### Total Time Worked
3:06

### Work Done  
Made the render Taylor Series Button actually work*  
I fixed multiple issues with the implementation of the shunting yard algorithm which were previously causing the miscalculation of certain expressions
Worked to get the GitHub repository in better working order:  
I added issues, tasks to the wiki, and a subproject for the component of the independent study I am working on right now.  
I linked my home Git commits to my GitHub username.   

### Learning Done
Today, I researched the exact implementation of the Shunting Yard algorithm especially with regards to operator precedence and associativity
