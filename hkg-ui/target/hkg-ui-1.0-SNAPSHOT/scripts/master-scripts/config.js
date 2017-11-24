var exampleAppConfig = {
    /* When set to false a query parameter is used to pass on the auth token.
     * This might be desirable if headers don't work correctly in some
     * environments and is still secure when using https. */
    useAuthTokenHeader: true
};
var corsConfig = {
    /*
     * please use http:// or https:// (if secure url available)
     *  it is necessary in url to identify absolute or relative url
     */
    masterHkgPath: '',
    centerHkgPath: '',
    isMaster: true
};