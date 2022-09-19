const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CopyPlugin = require('copy-webpack-plugin');

module.exports = {
  context: path.resolve(__dirname, 'src'),
  entry: './index.js',
  output: {
    path: path.resolve(__dirname, 'dist')
  },
  plugins: [
    new MiniCssExtractPlugin({
      // Options similar to the same options in webpackOptions.output
      // both options are optional
      filename: '[name].css',
      chunkFilename: '[id].css'
    }),
    new HtmlWebpackPlugin({
      template: 'index.html'
    }),
    new CopyPlugin({
      patterns: [
        { from: 'images', to: 'images' },
        { from: 'slides.md', to: 'slides.md' },
      ],
    }),
  ],
  module: {
    rules: [
      {
        test: /\.(sa|sc|c)ss$/,
        use: [
          'style-loader',
          MiniCssExtractPlugin.loader,
          'css-loader',
          'sass-loader?sourceMap',
        ],

      }, {
        test: /(eot|woff|woff2|ttf|svg)(\?\S*)?$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[name].[ext]',
              outputPath: 'fonts/',
              emitFile: true
            }
          }
        ]
      },
      {
        test: /\.(png|jpg|gif|svg|eot|ttf|woff|woff2)$/,
        loader: 'file-loader',
        options: {
          outputPath: 'images/',
        }
      },
      // packages .md files and extracts required images
      {
        test: /\.(md|markdown)$/,
        use: 'markdown-image-loader'
      }
    ],
  }
};
