import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import HttpApi from 'i18next-http-backend'; // fallback http load
import LanguageDetector from 'i18next-browser-languagedetector';

i18n
  .use(HttpApi)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    //debug: true, // for development
    debug: false, // for production
      defaultNS: 'translation',
      ns: ['translation'],
      backend: {
        loadPath: `/paw-2020b-2/locales/{{lng}}/{{ns}}.json`
      },
      load: 'unspecific',
      react: {
        wait: true
      },
  });
  export default i18n;