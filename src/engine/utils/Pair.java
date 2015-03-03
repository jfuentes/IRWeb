package engine.utils;

public class Pair<F, S>{

    public F first;
    public S second;

    public static <F, S> Pair<F, S> createPair(F key, S value) {
        return new Pair<F, S>(key, value);
    }

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

}
