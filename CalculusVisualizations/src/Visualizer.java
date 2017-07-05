public abstract class Visualizer {
    protected Function function;

    protected boolean showing = false;

    public abstract void draw();

    public void setFunction(Function function) {
        this.function = function;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }


}
