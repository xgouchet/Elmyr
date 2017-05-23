## Forger

The `Forger` class is the main class of the Elmyr library. 

### Resetting the seed

Like many random generators, the Forger class is based on a seed, which is a Long number. You can re-set the seed using the `reset(seed: Int)` method. 


### Generating Booleans

With forgers, you can generate Boolean values (ie : `true` or `false`). 

 - **fun aBool() : Bool**

    Returns a random boolean. Think coin toss.

 - **fun aBool(probability : Float = 0.5f) : Bool**

    Returns a random boolean, with a biased probability of returning `true`.

    - _probability_ : the probability for the result to be `true`. _0.0f_ means always `false`, _1.0f_ means always `true`.