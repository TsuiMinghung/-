import java.util.ArrayList;
import java.util.List;

public class Joint implements Simplify {

    public static Joint multiply(Joint j1,Joint j2) {
        Joint result = j1.clone();
        result.multiply(j2);
        return result;
    }

    private Constant constant;
    private final List<Power> powers;
    private final List<TriFunction> triFunctions;

    public Joint() {
        constant = Constant.one();
        powers = new ArrayList<>();
        triFunctions = new ArrayList<>();
    }

    public Joint(Constant c) {
        this.constant = c.clone();
        powers = new ArrayList<>();
        triFunctions = new ArrayList<>();
    }

    public Joint(Power p) {
        this.constant = Constant.one();
        powers = new ArrayList<Power>() {
            {
                add(p.clone());
            }
        };
        this.triFunctions = new ArrayList<>();
    }

    public Joint(TriFunction t) {
        this.constant = Constant.one();
        powers = new ArrayList<>();
        this.triFunctions = new ArrayList<TriFunction>() {
            {
                add(t.clone());
            }
        };
    }

    //used for clone
    public Joint(Joint other) {
        constant = other.constant.clone();
        powers = new ArrayList<>();
        triFunctions = new ArrayList<>();
        for (Power p : other.powers) {
            powers.add(p.clone());
        }
        for (TriFunction t : other.triFunctions) {
            triFunctions.add(t.clone());
        }
    }

    public void multiply(Joint other) {
        constant.multiply(other.constant.clone());
        for (Power p : other.powers) {
            multiplyPow(p.clone());
        }
        for (TriFunction t : other.triFunctions) {
            multiplyTri(t.clone());
        }
    }

    public Joint clone() {
        return new Joint(this);
    }

    public void multiplyConstant(Constant c) {
        constant.multiply(c);
    }

    public void multiplyTri(TriFunction t) {
        if (t.isOne()) {
            return;
        }
        if (t.isZero()) {
            setToZero();
        }
        TriFunction current = triFind(t);
        if (current != null) {
            current.multiply(t);
        } else {
            triFunctions.add(t.clone());
        }
    }

    public void multiplyPow(Power p) {
        if (p.isOne()) {
            return;
        }
        Power current = powerFind(p.getBase());
        if (current != null) {
            current.multiply(p);
        } else {
            powers.add(p.clone());
        }
    }

    private void setToZero() {
        constant = Constant.zero();
        powers.clear();
        triFunctions.clear();
    }

    private TriFunction triFind(TriFunction tri) {
        for (TriFunction t : triFunctions) {
            if (tri.bodyEquals(t)) {
                return t;
            }
        }
        return null;
    }

    private Power powerFind(String base) {
        for (Power power : powers) {
            if (power.baseEquals(base)) {
                return power;
            }
        }
        return null;
    }

    public boolean isZero() {
        if (constant.isZero()) {
            return true;
        }
        for (TriFunction t : triFunctions) {
            if (t.isZero()) {
                return true;
            }
        }
        return false;
    }

    private void setToOne() {
        constant = Constant.one();
        powers.clear();
        triFunctions.clear();
    }

    public boolean isOne() {
        if (constant.isOne()) {
            for (Power p : powers) {
                if (!p.isOne()) {
                    return false;
                }
            }
            for (TriFunction t : triFunctions) {
                if (!t.isOne()) {
                    return false;
                }
            }
            setToOne();
            return true;
        } else {
            return false;
        }
    }

    private boolean excludePow(Power other) {
        for (Power power : powers) {
            if (power.equals(other)) {
                return false;
            }
        }
        return true;
    }

    private boolean excludeTri(TriFunction other) {
        for (TriFunction tri : triFunctions) {
            if (tri.equals(other)) {
                return false;
            }
        }
        return true;
    }

    private boolean powerEquals(Joint other) {
        for (Power p1 : powers) {
            if (other.excludePow(p1)) {
                return false;
            }
        }

        for (Power p2 : other.powers) {
            if (this.excludePow(p2)) {
                return false;
            }
        }
        return other.powers.size() == powers.size();
    }

    private boolean triEquals(Joint other) {
        for (TriFunction t1 : other.triFunctions) {
            if (this.excludeTri(t1)) {
                return false;
            }
        }

        for (TriFunction t2 : other.triFunctions) {
            if (this.excludeTri(t2)) {
                return false;
            }
        }
        return other.triFunctions.size() == triFunctions.size();
    }

    public boolean bodyEquals(Joint other) {
        return powerEquals(other) && triEquals(other);
    }

    public boolean equals(Joint other) {
        return constant.equals(other.constant) && bodyEquals(other);
    }

    public void negate() {
        constant.negate();
    }

    public void add(Joint j) {
        assert (bodyEquals(j));
        constant.add(j.constant);
        if (constant.isZero()) {
            setToZero();
        }
    }

    public boolean isNegative() {
        return constant.isNegative();
    }

    @Override
    public String simplify() {
        if (!powers.isEmpty() || !triFunctions.isEmpty()) {
            StringBuilder result = new StringBuilder(filter(constant.simplify()));
            if (powers.isEmpty()) {
                result.append(simplifyTri());
            } else if (triFunctions.isEmpty()) {
                result.append(simplifyPow());
            } else {
                result.append(simplifyPow()).append("*").append(simplifyTri());
            }
            return result.toString();
        }
        return constant.simplify();
    }

    private String filter(String prefix) {
        switch (prefix) {
            case "+1":
            case "1":
                return "";
            case "-1":
                return "-";
            default:
                return prefix + "*";
        }
    }

    private String simplifyPow() {
        if (powers.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder(powers.get(0).simplify());
        for (int i = 1;i < powers.size();++i) {
            result.append("*").append(powers.get(i).simplify());
        }
        return result.toString();
    }

    private String simplifyTri() {
        if (triFunctions.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder(triFunctions.get(0).simplify());
        for (int i = 1;i < triFunctions.size();++i) {
            result.append("*").append(triFunctions.get(i).simplify());
        }
        return result.toString();
    }

    public boolean onlyNumber() {
        return powers.isEmpty() && triFunctions.isEmpty();
    }

    public boolean onlyPower() {
        return constant.isOne() && triFunctions.isEmpty() && powers.size() == 1;
    }
}
