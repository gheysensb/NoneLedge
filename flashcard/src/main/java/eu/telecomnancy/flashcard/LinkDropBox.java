package eu.telecomnancy.flashcard;
import com.dropbox.core.*;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.DbxException;

import java.io.*;
import java.util.ArrayList;

import com.dropbox.core.DbxException;


public class LinkDropBox {
    private static final String ACCESS_TOKEN = "sl.BWVUsdbJTI65JsA0_DBfWJgaG222nPnL01jCir7QG1obM-xV1r9bAubeFF3pIDPQmO4p_24K22cYQ5_ib7qxJt9HozOxw_Sj7O385TDlWJsbmKXoGSI0fkmRSDGkP1-GZhvV-bEQvqVy";
    private DbxClientV2 client;
    private String url;
    private DbxRequestConfig config;
    private DbxWebAuth auth_flow;

    /**
     * Créer un flux vers Dropbox et obtenir l'autorisation pour exporter/importer des fichiers
     * @throws DbxException
     * @throws IOException
     */
    public LinkDropBox() throws DbxException, IOException {
        String ACCESS_APP = "d7t8ubhmhwe1rg9";
        String SECRET_APP = "eud1s1qgkcea8dr";
        /* create dropbox auth_flow */
        DbxAppInfo appInfo = new DbxAppInfo(ACCESS_APP, SECRET_APP);
        config = new DbxRequestConfig("flashcard");
        /*get the authorization url with DbxWebAuth */
        auth_flow = new DbxWebAuth(config,appInfo );
        String authorizeUrl = auth_flow.authorize(DbxWebAuth.newRequestBuilder().withNoRedirect().build());
        url = authorizeUrl;
    }

    /**
     * Cette méthode permet la connexion à DropBox
     * @param code
     * @throws DbxException
     */
    public void connect(String code) throws DbxException {
            DbxAuthFinish authFinish = auth_flow.finishFromCode(code);
            String accessToken = authFinish.getAccessToken();
            /* create the client */
            this.client = new DbxClientV2(config, accessToken);
            /*get the account information */
            FullAccount account = this.client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());

    }

    public String getUrl() {
        return url;
    }

    /**
     * Cette méthode permet d'envoyer des piles au format JSON sur le serveur DropBox
     * @param nomfichier nom du fichier de la pile à envoyer
     */
    public void send(String nomfichier) throws RuntimeException {
        try {
            InputStream in = new FileInputStream("src/main/resources/eu/telecomnancy/flashcard/upload/"+nomfichier);
            FileMetadata metadata = client.files().uploadBuilder("/stockage/"+nomfichier).uploadAndFinish(in);
        } catch (FileNotFoundException e) {
            try{
                String homepath = System.getProperty("user.home");
                InputStream in = new FileInputStream(homepath+"/NoneLedge/upload/"+nomfichier);
                FileMetadata metadata = client.files().uploadBuilder("/stockage/"+nomfichier).uploadAndFinish(in);
            }
            catch(FileNotFoundException a){
                a.printStackTrace();
            } catch (UploadErrorException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (DbxException ex) {
                throw new RuntimeException(ex);
            }
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode permet de télécharger des fichiers au format JSON d'un serveur DropBox pour pouvoir les importer ensuite
     * @param nomfichier nom du fichier à télécharger du serveur
     */
    public void download(String nomfichier) {
        /* dowload from the client dropbox the file nomfichier in the resources folder */
        try {
            InputStream in = client.files().downloadBuilder("/stockage/" + nomfichier).start().getInputStream();
            //InputStream in = new FileInputStream(nomfichier);
            FileMetadata metadata = client.files().uploadBuilder("/src/main/resources/eu/telecomnancy/flashcard/upload/" + nomfichier).uploadAndFinish(in);
        }
        catch (FileNotFoundException e){
            try{
                InputStream in = client.files().downloadBuilder("/stockage/" + nomfichier).start().getInputStream();
                //InputStream in = new FileInputStream(nomfichier);
                String homepath = System.getProperty("user.home");
                FileMetadata metadata = client.files().uploadBuilder(homepath+"/NoneLedge/upload/" + nomfichier).uploadAndFinish(in);
            }
            catch (FileNotFoundException a){
                a.printStackTrace();
            } catch (UploadErrorException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (DbxException ex) {
                throw new RuntimeException(ex);
            }
        }
        catch (DbxException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> Viewer() throws DbxException {
        ArrayList<String> list = new ArrayList<String>();
        ListFolderResult result = client.files().listFolder("/stockage");
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                list.add(metadata.getName());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }
        return list;
    }
}
