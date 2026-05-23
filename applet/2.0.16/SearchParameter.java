public class SearchParameter {

        private int statusCode;
        private String joken;
        /*
         *  0: 検索実行前
         *  1: 検索実行直後(結果を表示)
         * -1: 検索終了後
         */


        public SearchParameter() {
          statusCode = 0;
          joken = "";
        }


        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
        public int getStatusCode() {
            return statusCode;
        }
        
        public void setJoken(String str) {
            this.joken = str;
        }
        

        public String getJoken() {
            if (this.joken.equals("")) return new String("条件なし");
            else return this.joken;
        }

}
