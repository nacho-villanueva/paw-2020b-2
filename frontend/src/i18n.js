import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import Backend from 'i18next-chained-backend';
import LocalStorageBackend from 'i18next-localstorage-backend'; // primary use cache
import HttpApi from 'i18next-http-backend'; // fallback http load
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
  .use(Backend)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    backend: {
      backends: [
        LocalStorageBackend,  // primary
        HttpApi               // fallback
      ],
      backendOptions: [{
        // expiration
        expirationTime: 0 // Use this value for development (check changes to translation files)
        //expirationTime: 7*24*60*60*1000 // Use this value for production
      }, {
      }]
    },
    fallbackLng: 'en',
    //debug: true, // for development
    debug: false, // for production
    detection: {
        order: ['navigator', 'cookie'],
        cache: ['cookie']
    },
    interpolation: {
      escapeValue: false, // not needed for react as it escapes by default
    }
  });

  export default i18n;