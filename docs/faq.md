## F.A.Q.

 - **Why isn't there more configuration available to generate data ?**

The aim of this library is to forge data that could pass as real data, as simply as possible. This is meant as a fuzzing 
source, but not necessarily as a property based testing framework.

 - **Sometimes data generation fails because of precondition errors (eg: max < min)**
 
This can happen when the input for forger generation is also random. You can use the `forger.ignorePreconditionsErrors`
value to ignore tests where this happen, instead of making them fail.