import java.util.ArrayList;
import java.util.List;

public class JointList implements Simplify {

    public static JointList multiply(JointList l1,JointList l2) {
        JointList result = new JointList();
        for (Joint j1 : l1.contents) {
            if (j1.isZero()) {
                continue;
            }
            for (Joint j2 : l2.contents) {
                if (j2.isZero()) {
                    continue;
                }
                result.addJoint(Joint.multiply(j1.clone(),j2.clone()));
            }
        }
        return result;
    }

    private List<Joint> contents;

    public JointList() {
        contents = new ArrayList<>();
    }

    //used for clone
    public JointList(JointList other) {
        contents = new ArrayList<>();
        for (Joint j : other.contents) {
            contents.add(j.clone());
        }
    }

    public void addAll(JointList other) {
        for (Joint j : other.contents) {
            addJoint(j);
        }
    }

    public void addJoint(Joint j) {
        Joint current = bodyEqualsFind(j);
        if (current != null) {
            current.add(j);
        } else {
            contents.add(j.clone());
        }
    }

    private Joint bodyEqualsFind(Joint j) {
        for (Joint own : contents) {
            if (j.bodyEquals(own)) {
                return own;
            }
        }
        return null;
    }

    private Joint equalsFind(Joint j) {
        for (Joint own : contents) {
            if (j.equals(own)) {
                return own;
            }
        }
        return null;
    }

    public int size() {
        return contents.size();
    }

    public Joint get(int index) {
        return contents.get(index);
    }

    public JointList clone() {
        return new JointList(this);
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public String simplify() {
        if (contents.isEmpty()) {
            return "0";
        } else {
            StringBuilder result = new StringBuilder();
            if (!contents.get(0).isZero()) {
                result.append(contents.get(0).simplify());
            }
            for (int i = 1;i < contents.size();++i) {
                if (contents.get(i).isZero()) {
                    continue;
                }
                result.append(contents.get(i).isNegative() ? "" : "+");
                result.append(contents.get(i).simplify());
            }
            if (result.length() == 0) {
                result.append("0");
            }
            return result.toString();
        }
    }

    public boolean isZero() {
        for (Joint j : contents) {
            if (!j.isZero()) {
                return false;
            }
        }
        return true;
    }

    public boolean isOne() {
        boolean flag = false;
        for (Joint j : contents) {
            if (j.isOne()) {
                if (flag) {
                    return false;
                } else {
                    flag  = true;
                }
            } else if (j.isZero()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean onlyNumber() {
        if (contents.isEmpty()) {
            return true;
        } else if (contents.size() == 1) {
            return contents.get(0).onlyNumber();
        } else {
            //assume every number has been merged
            return false;
        }
    }

    public boolean onlyPower() {
        if (contents.size() == 1) {
            return contents.get(0).onlyPower();
        } else {
            return false;
        }
    }

    public boolean equals(JointList other) {
        for (int i = 0;i < other.size();++i) {
            if (equalsFind(other.get(i)) == null) {
                return false;
            }
        }
        for (Joint content : contents) {
            if (other.equalsFind(content) == null) {
                return false;
            }
        }
        return other.contents.size() == contents.size();
    }
}
