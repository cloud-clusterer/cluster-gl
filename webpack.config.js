const KotlinWebpackPlugin = require('@jetbrains/kotlin-webpack-plugin');

module.exports = {
  entry: 'cluster-gl',

  resolve: {
    modules: ['bin/test', 'node_modules'],
  },

  output: {
    path: __dirname + '/bin/bundle',
    filename: 'cluster-gl.js',
  },

  plugins: [
    new KotlinWebpackPlugin({
      src: __dirname,
      output: 'bin/test',
      moduleName: 'cluster-gl',
      librariesAutoLookup: true,
      packagesContents: [require('./package.json')],
    }),
    new KotlinWebpackPlugin({
          src: __dirname + '/src',
          output: 'bin/build',
          moduleName: 'cluster-gl',
          sourceMapEmbedSources: 'always',
          metaInfo: true,
          sourceMaps: true,
          librariesAutoLookup: true,
          packagesContents: [require('./package.json')],
        }),
  ],
};