**Only for Roots 1.12.2 3.1.7-3.1.8** 

This Fork fixes NullPointerException while opening stuff GUI.
Thats not the best method of this, but it should work.

I think this happens because in class `UUIDRegistry` server is always **__null__**, which leads to triggering **if(storage == null)**, and throws NullPointerException
I just changed method `UUIDRegistry#getDataInternal(UUID id)` so it just returns __**null**__, instaed of throwing exception.
