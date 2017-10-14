

### Principals

* Actors can only be created by other actors—where the top-level actor is provided by the library—and each created actor is supervised by its parent.
* Everything in Akka is designed to work in a distributed setting.
* Dead letters are not propagated over the network, if you want to collect them in one place you will have to subscribe one actor per network node and forward them manually.
* putting Akka on the boot class path will yield `NullPointerException` from strange places
* An `ActorRef` always represents an incarnation (path and UID) not just a given path. Therefore if an actor is stopped and a new one with the same name is created an `ActorRef` of the old incarnation will not point to the new one. An `ActorRef` always represents an incarnation (path and UID) not just a given path. Therefore if an actor is stopped and a new one with the same name is created an `ActorRef` of the old incarnation will not point to the new one.
* The `ActorRef` is immutable and has a one to one relationship with the Actor it represents. The `ActorRef` is also serializable and network-aware. This means that you can serialize it, send it over the wire and use it on a remote host and it will still be representing the same Actor on the original node, across the network.
* It is always preferable to communicate with other Actors using their `ActorRef` instead of relying upon `ActorSelection`.
* Since stopping an actor is asynchronous, you cannot immediately reuse the name of the child you just stopped; this will result in an `InvalidActorNameException`. Instead, `watch` the terminating actor and create its replacement in response to the `Terminated` message which will eventually arrive.

* The same actor system can never join a cluster again once it's been removed from that cluster. To re-join an actor system with the same `hostname:port` to a cluster you have to stop the actor system and start a new one with the same `hotname:port` which will then receive a different UID.
* Currently the only possible partition points are _routed_ actors.

### Key principles of actor model

* No shared state
* Lightweight processes
* Asynchronous message-passing
* Mailboxes to buffer incoming messages
* Mailbox processing with pattern matching


