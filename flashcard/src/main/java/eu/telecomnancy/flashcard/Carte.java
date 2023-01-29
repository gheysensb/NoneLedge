package eu.telecomnancy.flashcard;

public class Carte {
    private String questionTexte;
    private String questionImage;
    private String answer;
    private boolean connu;

    public Carte(String questionTexte, String questionImage,String answer) {
        this.questionTexte = questionTexte;
        this.questionImage = questionImage;
        this.answer = answer;
        this.connu = false;
    }

    public String getQuestionTexte() {
        return questionTexte;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isConnu(){return this.connu;}

    public void setConnu(boolean connu){this.connu = connu;}

    public void setQuestion(String question) {
        this.questionTexte = question;
    }

    public void setImage(String image) {
        this.questionImage = image;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
