public class Member {

        private String name = null;
        private String sex = null;
        private String blood = null;
        private String birthday = null;
        private String prefectures = null;
        private String job = null;
        private String interest = null;
        private String keyword = null;
        private String email = null;
        private String url = null;

        public void setName(String name){
            this.name = name;   
        }
        public void setSex(String sex){
            this.sex = sex;   
        }
        public void setBlood(String blood){
            this.blood = blood;   
        }
        public void setBirthday(String birthday){
            this.birthday = birthday;   
        }
        public void setPrefectures(String prefectures){
            this.prefectures = prefectures;   
        }
        public void setJob(String job){
            this.job = job;   
        }
        public void setInterest(String interest){
            this.interest = interest;   
        }
        public void setKeyword(String keyword){
            this.keyword = keyword;   
        }
        public void setEmail(String email){
            this.email = email;   
        }
        public void setUrl(String url){
            this.url = url;   
        }
        
        
        public String getName() {
            if (name == null) return "";
            return name;   
        }
        public String getSex() {
            if (sex == null) return "";
            return sex;   
        }
        public String getBlood() {
            if (blood == null) return "";
            return blood;   
        }
        public String getBirthday() {
            if (birthday == null) return "";
            return birthday;   
        }
        public String getPrefectures() {
            if (prefectures == null) return "";
            return prefectures;   
        }
        public String getJob() {
            if (job == null) return "";
            return job;   
        }
        public String getInterest() {
            if (interest == null) return "";
            return interest;   
        }
        public String getKeyword() {
            if (keyword == null) return "";
            return keyword;   
        }
        public String getEmail() {
            if (email == null) return "";
            return email;   
        }
        public String getUrl() {
            if (url == null) return "";
            return url;   
        }
     
}
