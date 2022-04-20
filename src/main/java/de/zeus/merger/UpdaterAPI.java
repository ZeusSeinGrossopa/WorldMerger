package de.zeus.merger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

public class UpdaterAPI {
 
  
 ​    ​public​ ​static​ ​final​ ​String​ ​GITHUB_CUSTOM_URL​ = ​"https://api.github.com/repos/%s/releases/latest"​; 
  
 ​    ​private​ ​static​ ​File​ ​updaterFile​ = ​null​; 
 ​    ​private​ ​static​ ​boolean​ ​autoDelete​ = ​false​; 
  
 ​    ​private​ ​static​ ​File​ ​jarPath​; 
  
 ​    ​public​ ​static​ ​void​ ​downloadUpdater​(​File​ ​destination​) { 
 ​        ​downloadUpdater​(​destination​, ​null​); 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​downloadUpdater​(​File​ ​destination​, ​Consumer​<​File​> ​consumer​) { 
 ​        ​destination​ = ​new​ ​File​((​destination​.​isDirectory​() ? ​new​ ​File​(​destination​.​getAbsolutePath​()) + ​"/Updater.jar"​ : ​destination​.​getAbsolutePath​())); 
  
 ​        ​final​ ​File​ ​finalDestination​ = ​destination​; 
 ​        ​updaterFile​ = ​finalDestination​; 
  
 ​        ​if​ (​autoDelete​) { 
 ​            ​if​ (​destination​.​exists​()) 
 ​                ​destination​.​delete​(); 
  
 ​            ​if​ (​consumer​ != ​null​) 
 ​                ​consumer​.​accept​(​destination​); 
 ​            ​return​; 
 ​        } 
  
 ​        ​getLatestReleaseFromGithub​(​"ZeusSeinGrossopa"​, ​"UpdaterAPI"​, ​callback​ -> { 
 ​            ​try​ { 
 ​                ​URL​ ​url​ = ​new​ ​URL​(​callback​[​1​]); 
  
 ​                ​FileUtils​.​copyURLToFile​(​url​, ​finalDestination​); 
  
 ​                ​if​ (​consumer​ != ​null​) 
 ​                    ​consumer​.​accept​(​finalDestination​); 
 ​            } ​catch​ (​IOException​ ​e​) { 
 ​                ​e​.​printStackTrace​(); 
 ​            } 
 ​        }); 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​getLatestReleaseFromGithub​(​String​ ​githubUser​, ​String​ ​repository​, ​Consumer​<​String​[]> ​consumer​) { 
 ​        ​try​ { 
 ​            ​HttpURLConnection​ ​connect​ = (​HttpURLConnection​) ​new​ ​URL​(​String​.​format​(​GITHUB_CUSTOM_URL​, ​githubUser​ + ​"/"​ + ​repository​)).​openConnection​(); 
  
 ​            ​connect​.​setConnectTimeout​(​10000​); 
  
 ​            ​connect​.​setRequestProperty​(​"Accept"​, ​"application/vnd.github.v3+json"​); 
 ​            ​connect​.​setRequestProperty​(​"Content-Type"​, ​"application/json"​); 
  
 ​            ​connect​.​setRequestProperty​(​"User-Agent"​, ​githubUser​ + ​"/"​ + ​repository​ + ​" ("​ + ​System​.​getProperty​(​"os.name"​) + ​"; "​ + ​System​.​getProperty​(​"os.arch"​) + ​")"​); 
  
 ​            ​connect​.​connect​(); 
  
 ​            ​InputStream​ ​in​ = ​connect​.​getInputStream​(); 
 ​            ​BufferedReader​ ​reader​ = ​new​ ​BufferedReader​(​new​ ​InputStreamReader​(​in​, ​StandardCharsets​.​UTF_8​)); 
  
 ​            ​if​ (​connect​.​getResponseCode​() == ​200​) { 
 ​                ​JsonObject​ ​object​ = ​JsonParser​.​parseReader​(​reader​).​getAsJsonObject​(); 
  
 ​                ​String​ ​downloadLink​ = ​object​.​entrySet​().​stream​().​filter​(​e​ -> ​e​.​getKey​().​equals​(​"assets"​)) 
 ​                        .​map​(​Map​.​Entry​::​getValue​).​findFirst​().​orElseThrow​(() -> ​new​ ​RuntimeException​(​"Can not update system"​)) 
 ​                        .​getAsJsonArray​() 
 ​                        .​get​(​0​).​getAsJsonObject​().​get​(​"browser_download_url"​).​getAsString​(); 
  
 ​                ​consumer​.​accept​(​new​ ​String​[]{​object​.​get​(​"tag_name"​).​getAsString​(), ​downloadLink​}); 
 ​            } 
 ​        } ​catch​ (​Exception​ ​e​) { 
 ​            ​e​.​printStackTrace​(); 
 ​        } 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​update​(​String​ ​url​, ​File​ ​newFile​) ​throws​ ​IOException​ { 
 ​        ​if​ (​updaterFile​ == ​null​) 
 ​            ​throw​ ​new​ ​NullPointerException​(​"The downloadUpdater must be called before using this method. Alternate use the #update(updaterFile, url, newFile) method."​); 
  
 ​        ​update​(​updaterFile​, ​url​, ​newFile​); 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​update​(​String​ ​url​, ​File​ ​newFile​, ​boolean​ ​restart​) ​throws​ ​IOException​ { 
 ​        ​if​ (​updaterFile​ == ​null​) 
 ​            ​throw​ ​new​ ​NullPointerException​(​"The downloadUpdater must be called before using this method. Alternate use the #update(updaterFile, url, newFile) method."​); 
  
 ​        ​update​(​updaterFile​, ​url​, ​newFile​, ​restart​); 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​update​(​File​ ​updaterFile​, ​String​ ​url​, ​File​ ​newFile​) ​throws​ ​IOException​ { 
 ​        ​update​(​updaterFile​, ​getJarPath​(), ​url​, ​newFile​, ​false​); 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​update​(​File​ ​updaterFile​, ​String​ ​url​, ​File​ ​newFile​, ​boolean​ ​restart​) ​throws​ ​IOException​ { 
 ​        ​update​(​updaterFile​, ​getJarPath​(), ​url​, ​newFile​, ​restart​); 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​update​(​File​ ​updaterFile​, ​File​ ​oldFile​, ​String​ ​url​, ​File​ ​newFile​, ​boolean​ ​restart​) ​throws​ ​IOException​ { 
 ​        ​String​ ​javaBin​ = ​System​.​getProperty​(​"java.home"​) + ​File​.​separator​ + ​"bin"​ + ​File​.​separator​ + ​"java"​; 
  
 ​        ​ProcessBuilder​ ​builder​ = ​new​ ​ProcessBuilder​(​javaBin​, ​"-jar"​, ​updaterFile​.​getAbsolutePath​(), ​url​, ​oldFile​.​getAbsolutePath​(), ​newFile​.​getAbsolutePath​(), ​restart​ ? ​"true"​ : ​""​); 
  
 ​        ​if​ (​autoDelete​) { 
 ​            ​autoDelete​ = ​false​; 
 ​            ​downloadUpdater​(​updaterFile​, ​file​ -> { 
 ​                ​try​ { 
 ​                    ​builder​.​start​(); 
 ​                } ​catch​ (​IOException​ ​e​) { 
 ​                    ​e​.​printStackTrace​(); 
 ​                } 
 ​            }); 
  
 ​            ​autoDelete​ = ​true​; 
 ​        } ​else​ { 
 ​            ​builder​.​start​(); 
 ​        } 
 ​    } 
  
 ​    ​public​ ​static​ ​boolean​ ​needUpdate​(​String​ ​version1​, ​String​ ​version2​) { 
 ​        ​return​ ​compareVersions​(​version1​, ​version2​) == -​1​; 
 ​    } 
  
 ​    ​public​ ​static​ ​int​ ​compareVersions​(​String​ ​version1​, ​String​ ​version2​) { 
 ​        ​String​[] ​levels1​ = ​version1​.​split​(​"\\."​); 
 ​        ​String​[] ​levels2​ = ​version2​.​split​(​"\\."​); 
  
 ​        ​int​ ​length​ = ​Math​.​max​(​levels1​.​length​, ​levels2​.​length​); 
 ​        ​for​ (​int​ ​i​ = ​0​; ​i​ < ​length​; ​i​++) { 
 ​            ​Integer​ ​v1​ = ​i​ < ​levels1​.​length​ ? ​Integer​.​parseInt​(​levels1​[​i​]) : ​0​; 
 ​            ​Integer​ ​v2​ = ​i​ < ​levels2​.​length​ ? ​Integer​.​parseInt​(​levels2​[​i​]) : ​0​; 
 ​            ​int​ ​compare​ = ​v1​.​compareTo​(​v2​); 
 ​            ​if​ (​compare​ != ​0​) { 
 ​                ​return​ ​compare​; 
 ​            } 
 ​        } 
 ​        ​return​ ​0​; 
 ​    } 
  
 ​    ​public​ ​static​ ​File​ ​getJarPath​() { 
 ​        ​if​ (​jarPath​ == ​null​) { 
 ​            ​try​ { 
 ​                ​return​ ​new​ ​File​(​UpdaterAPI​.​class​.​getProtectionDomain​().​getCodeSource​().​getLocation​().​toURI​().​getPath​()).​getAbsoluteFile​(); 
 ​            } ​catch​ (​URISyntaxException​ ​e​) { 
 ​                ​e​.​printStackTrace​(); 
 ​            } 
 ​        } 
  
 ​        ​return​ ​jarPath​; 
 ​    } 
  
 ​    ​public​ ​static​ ​void​ ​setAutoDelete​(​boolean​ ​value​) { 
 ​        ​autoDelete​ = ​value​; 
 ​    } 
  
 ​    ​public​ ​static​ ​File​ ​getCurrentUpdater​() { 
 ​        ​return​ ​updaterFile​; 
 ​    }
}
