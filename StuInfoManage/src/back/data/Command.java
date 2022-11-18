package back.data;

public class Command {
    //��������
    private String type;
    //��ǰ��Ϣ
    private Student prestu;
    //������Ϣ
    private Student curstu;

    public Command(String type, Student prestu, Student curstu) {
        this.type = type;
        this.prestu = prestu;
        this.curstu = curstu;
    }

    @Override
    public String toString() {
        return type + "\n" + prestu.toString() + curstu.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Student getPrestu() {
        return prestu;
    }

    public void setPrestu(Student prestu) {
        this.prestu = prestu;
    }

    public Student getCurstu() {
        return curstu;
    }

    public void setCurstu(Student curstu) {
        this.curstu = curstu;
    }
}
