# Floating Action Menu [![](https://jitpack.io/v/ueen/FloatingActionMenu.svg)](https://jitpack.io/#ueen/FloatingActionMenu)

100% Kotlin

![ezgif-4-c8a35ef29f21](https://user-images.githubusercontent.com/5067479/126224971-c5b77066-0783-4384-9da1-99efbd77f14a.gif)

## Implementation

Step 1. Add the JitPack repository to your build file 
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency
```groovy
dependencies {
	implementation 'com.github.ueen:FloatingActionMenu:1.1'
}
```


## Example

add to xml
```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <de.ueen.fabmenu.FloatingActionMenu
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="end|bottom"
        app:icon="@drawable/ic_baseline_filter_list_24"
        app:useCompatPadding="true" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```


Next in your Activity/Fragment setup like this
```kotlin
val fam: FloatingActionMenu = view.findViewById(R.id.fam)

fam.addAction(FloatingActionMenu.ActionItem(R.drawable.ic_baseline_filter_list_24,"first"))
fam.addAction(FloatingActionMenu.ActionItem(R.drawable.ic_baseline_edit_24,"second"))
fam.addAction(FloatingActionMenu.ActionItem(R.drawable.ic_baseline_done_24,"third"))

fam.selectActionItemToFab("first")

fam.setOnActionClickListener(selectable = true) { actionItem ->
    Log.d("FloatingActionMenu", actionItem.tag)
}
```

### Example
Theres an example App in the app module, check it out!

![ezgif-4-4ebd23936524](https://user-images.githubusercontent.com/5067479/126225107-787db460-581b-4755-b50e-afb84f49d321.gif)


### License
```
Copyright 2021 ueen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
