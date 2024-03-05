# Acrossed
An algorithm to generate dense 5x5 crosswords, NYT style

## How it will work:

An algorithm that searches words, and gets probabilities of next letters can be used to determine the intersection between likely characters between the down, and across

Lets take the following example

```
X P
R
```

The next most probable character for P is probably an L, (Many words probably start with PL) but for R, there is barely any words that start with an "RL". So even though L may be the most probabilities for P, it is not for R, so this is crossed out

Lets define a probabilities table for these letters, and some others for future use(i don't know how accurate this is but just to demonstrate):

(In the actual program itself, these probabilities will be variable, and will be determined with the remaining words left with the next letter probabilies.)

```
R    | P   | L    | A   | E   | O   | N   |
-------------------------------------
O    | L   | O    | N   | A   | A   | A   |
A    | H   | E    | I   | N   | N   | O   |
E    | A   | A    | P   | D   | M   | I   |
U    | R   | U    | L   | R   | T   | E   |
I    | E   | I    | S   | B   | R   | U   |
```

We would try A, since it is the best of the worst position (3)

So now, we have

```
X P
R A
```

And so we iterate for each time, starting the iterations in the corners then iterating throughout

So here what each step would look like

Iter 2:

```
X P L
R A
```

Iter 3:

```
X P L
R A
O
```

Iter 4: (We have another two way intersection with L and A)

Except... a small issue, according to the table, there aren't really any words that have L and A in common

So we iterate backwards to the iteration where the A was made and choose the next probability (an E)

```
X P
R E
```

Now we have 2 letters in common

```
X P L
R E
```

```
X P L
R E
O
```

```
X P L
R E A
O
```

```
X P L
R E A
O A
```

```
X P L
R E A
O A P
```

...Next

```
X P L O
R E A
O A P
```

```
X P L O
R E A
O A P
A
```

```
X P L O
R E A N
O A P
A
```

```
X P L O
R E A N
O A P
A N
```

```
X P L O
R E A N
O A P A
A N
```

```
X P L O
R E A N
O A P A
A N A
```

```
X P L O
R E A N
O A P A
A N A N
```

..Next (skipping iteration to end result)

```
X P L O A
R E A N I
O A P A N
A N A N A
N A N A X
```


So now, we shpuld have a valid 5x5 crossword (It is not valid here since the letter probabilities are static in here)


The time it takes to execute this algorithm (assuming its a 5x5):

Best case: O(n) = 36
Average case: No clue
Worst case: Probably a really long time
