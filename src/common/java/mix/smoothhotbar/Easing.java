package mix.smoothhotbar;

public enum Easing {
    LINEAR,
    EASE_OUT,
    EASE_IN_OUT,
    EXPO,
    BACK,
    ELASTIC;

    public float apply(float t) {
        if (t <= 0f) {
            return 0f;
        }
        if (t >= 1f) {
            return 1f;
        }
        switch (this) {
            case EASE_OUT: {
                float u = 1f - t;
                return 1f - u * u * u;
            }
            case EASE_IN_OUT: {
                if (t < 0.5f) {
                    return 4f * t * t * t;
                }
                float u = -2f * t + 2f;
                return 1f - u * u * u / 2f;
            }
            case EXPO:
                return 1f - (float) Math.pow(2.0, -10.0 * t);
            case BACK: {
                float u = t - 1f;
                return 1f + 2.70158f * u * u * u + 1.70158f * u * u;
            }
            case ELASTIC:
                return (float) (Math.pow(2.0, -10.0 * t) * Math.sin((t * 10.0 - 0.75) * (2.0 * Math.PI / 3.0)) + 1.0);
            case LINEAR:
            default:
                return t;
        }
    }
}
