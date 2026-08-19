package org.example.todointership;


public class Task {

    private static long id=0;
    private String title;
    private boolean done;

    Task(String title) {
id++;
        this.title = title;
        done = false;

    }

    public  long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done;
    }

    public void  setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
