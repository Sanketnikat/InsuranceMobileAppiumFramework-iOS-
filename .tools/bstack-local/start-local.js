const browserstack = require('browserstack-local');
const key = process.env.BROWSERSTACK_ACCESS_KEY;
const localIdentifier = process.env.BROWSERSTACK_LOCAL_IDENTIFIER;
if (!key) {
  console.error('BROWSERSTACK_ACCESS_KEY not set');
  process.exit(1);
}
const bs_local = new browserstack.Local();
const startOptions = { key: key, force: true, forcelocal: true, verbose: true };
if (localIdentifier) {
  startOptions.localIdentifier = localIdentifier;
}
bs_local.start(startOptions, function(error) {
  if (error) {
    console.error('LOCAL_START_FAILED', error);
    process.exit(1);
  }
  console.log('LOCAL_RUNNING', bs_local.isRunning());
  console.log('READY');
});
process.on('SIGTERM', () => { bs_local.stop(() => process.exit(0)); });
